import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { DeviceModelService } from 'app/entities/device-model/service/device-model.service';
import { IOption } from '../option.model';
import { OptionService } from '../service/option.service';
import { OptionFormService } from './option-form.service';

import { OptionUpdateComponent } from './option-update.component';

describe('Option Management Update Component', () => {
  let comp: OptionUpdateComponent;
  let fixture: ComponentFixture<OptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionFormService: OptionFormService;
  let optionService: OptionService;
  let vendorService: VendorService;
  let deviceModelService: DeviceModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OptionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    optionFormService = TestBed.inject(OptionFormService);
    optionService = TestBed.inject(OptionService);
    vendorService = TestBed.inject(VendorService);
    deviceModelService = TestBed.inject(DeviceModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Vendor query and add missing value', () => {
      const option: IOption = { id: 456 };
      const vendors: IVendor[] = [{ id: 12339 }];
      option.vendors = vendors;

      const vendorCollection: IVendor[] = [{ id: 27527 }];
      jest.spyOn(vendorService, 'query').mockReturnValue(of(new HttpResponse({ body: vendorCollection })));
      const additionalVendors = [...vendors];
      const expectedCollection: IVendor[] = [...additionalVendors, ...vendorCollection];
      jest.spyOn(vendorService, 'addVendorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ option });
      comp.ngOnInit();

      expect(vendorService.query).toHaveBeenCalled();
      expect(vendorService.addVendorToCollectionIfMissing).toHaveBeenCalledWith(
        vendorCollection,
        ...additionalVendors.map(expect.objectContaining),
      );
      expect(comp.vendorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DeviceModel query and add missing value', () => {
      const option: IOption = { id: 456 };
      const models: IDeviceModel[] = [{ id: 2643 }];
      option.models = models;

      const deviceModelCollection: IDeviceModel[] = [{ id: 19143 }];
      jest.spyOn(deviceModelService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceModelCollection })));
      const additionalDeviceModels = [...models];
      const expectedCollection: IDeviceModel[] = [...additionalDeviceModels, ...deviceModelCollection];
      jest.spyOn(deviceModelService, 'addDeviceModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ option });
      comp.ngOnInit();

      expect(deviceModelService.query).toHaveBeenCalled();
      expect(deviceModelService.addDeviceModelToCollectionIfMissing).toHaveBeenCalledWith(
        deviceModelCollection,
        ...additionalDeviceModels.map(expect.objectContaining),
      );
      expect(comp.deviceModelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const option: IOption = { id: 456 };
      const vendors: IVendor = { id: 511 };
      option.vendors = [vendors];
      const models: IDeviceModel = { id: 10352 };
      option.models = [models];

      activatedRoute.data = of({ option });
      comp.ngOnInit();

      expect(comp.vendorsSharedCollection).toContain(vendors);
      expect(comp.deviceModelsSharedCollection).toContain(models);
      expect(comp.option).toEqual(option);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 123 };
      jest.spyOn(optionFormService, 'getOption').mockReturnValue(option);
      jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: option }));
      saveSubject.complete();

      // THEN
      expect(optionFormService.getOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(optionService.update).toHaveBeenCalledWith(expect.objectContaining(option));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 123 };
      jest.spyOn(optionFormService, 'getOption').mockReturnValue({ id: null });
      jest.spyOn(optionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: option }));
      saveSubject.complete();

      // THEN
      expect(optionFormService.getOption).toHaveBeenCalled();
      expect(optionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 123 };
      jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(optionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVendor', () => {
      it('Should forward to vendorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vendorService, 'compareVendor');
        comp.compareVendor(entity, entity2);
        expect(vendorService.compareVendor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDeviceModel', () => {
      it('Should forward to deviceModelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deviceModelService, 'compareDeviceModel');
        comp.compareDeviceModel(entity, entity2);
        expect(deviceModelService.compareDeviceModel).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
