import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOtherDeviceType } from 'app/entities/other-device-type/other-device-type.model';
import { OtherDeviceTypeService } from 'app/entities/other-device-type/service/other-device-type.service';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IDeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';
import { DeviceModelFormService } from './device-model-form.service';

import { DeviceModelUpdateComponent } from './device-model-update.component';

describe('DeviceModel Management Update Component', () => {
  let comp: DeviceModelUpdateComponent;
  let fixture: ComponentFixture<DeviceModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceModelFormService: DeviceModelFormService;
  let deviceModelService: DeviceModelService;
  let otherDeviceTypeService: OtherDeviceTypeService;
  let vendorService: VendorService;
  let optionService: OptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DeviceModelUpdateComponent],
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
      .overrideTemplate(DeviceModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceModelFormService = TestBed.inject(DeviceModelFormService);
    deviceModelService = TestBed.inject(DeviceModelService);
    otherDeviceTypeService = TestBed.inject(OtherDeviceTypeService);
    vendorService = TestBed.inject(VendorService);
    optionService = TestBed.inject(OptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OtherDeviceType query and add missing value', () => {
      const deviceModel: IDeviceModel = { id: 456 };
      const otherDeviceType: IOtherDeviceType = { id: 1970 };
      deviceModel.otherDeviceType = otherDeviceType;

      const otherDeviceTypeCollection: IOtherDeviceType[] = [{ id: 14831 }];
      jest.spyOn(otherDeviceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: otherDeviceTypeCollection })));
      const additionalOtherDeviceTypes = [otherDeviceType];
      const expectedCollection: IOtherDeviceType[] = [...additionalOtherDeviceTypes, ...otherDeviceTypeCollection];
      jest.spyOn(otherDeviceTypeService, 'addOtherDeviceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deviceModel });
      comp.ngOnInit();

      expect(otherDeviceTypeService.query).toHaveBeenCalled();
      expect(otherDeviceTypeService.addOtherDeviceTypeToCollectionIfMissing).toHaveBeenCalledWith(
        otherDeviceTypeCollection,
        ...additionalOtherDeviceTypes.map(expect.objectContaining),
      );
      expect(comp.otherDeviceTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vendor query and add missing value', () => {
      const deviceModel: IDeviceModel = { id: 456 };
      const vendor: IVendor = { id: 7120 };
      deviceModel.vendor = vendor;

      const vendorCollection: IVendor[] = [{ id: 25284 }];
      jest.spyOn(vendorService, 'query').mockReturnValue(of(new HttpResponse({ body: vendorCollection })));
      const additionalVendors = [vendor];
      const expectedCollection: IVendor[] = [...additionalVendors, ...vendorCollection];
      jest.spyOn(vendorService, 'addVendorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deviceModel });
      comp.ngOnInit();

      expect(vendorService.query).toHaveBeenCalled();
      expect(vendorService.addVendorToCollectionIfMissing).toHaveBeenCalledWith(
        vendorCollection,
        ...additionalVendors.map(expect.objectContaining),
      );
      expect(comp.vendorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Option query and add missing value', () => {
      const deviceModel: IDeviceModel = { id: 456 };
      const options: IOption[] = [{ id: 27234 }];
      deviceModel.options = options;

      const optionCollection: IOption[] = [{ id: 1951 }];
      jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
      const additionalOptions = [...options];
      const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
      jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deviceModel });
      comp.ngOnInit();

      expect(optionService.query).toHaveBeenCalled();
      expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(
        optionCollection,
        ...additionalOptions.map(expect.objectContaining),
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deviceModel: IDeviceModel = { id: 456 };
      const otherDeviceType: IOtherDeviceType = { id: 1750 };
      deviceModel.otherDeviceType = otherDeviceType;
      const vendor: IVendor = { id: 29534 };
      deviceModel.vendor = vendor;
      const options: IOption = { id: 4506 };
      deviceModel.options = [options];

      activatedRoute.data = of({ deviceModel });
      comp.ngOnInit();

      expect(comp.otherDeviceTypesSharedCollection).toContain(otherDeviceType);
      expect(comp.vendorsSharedCollection).toContain(vendor);
      expect(comp.optionsSharedCollection).toContain(options);
      expect(comp.deviceModel).toEqual(deviceModel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceModel>>();
      const deviceModel = { id: 123 };
      jest.spyOn(deviceModelFormService, 'getDeviceModel').mockReturnValue(deviceModel);
      jest.spyOn(deviceModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceModel }));
      saveSubject.complete();

      // THEN
      expect(deviceModelFormService.getDeviceModel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceModelService.update).toHaveBeenCalledWith(expect.objectContaining(deviceModel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceModel>>();
      const deviceModel = { id: 123 };
      jest.spyOn(deviceModelFormService, 'getDeviceModel').mockReturnValue({ id: null });
      jest.spyOn(deviceModelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceModel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceModel }));
      saveSubject.complete();

      // THEN
      expect(deviceModelFormService.getDeviceModel).toHaveBeenCalled();
      expect(deviceModelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeviceModel>>();
      const deviceModel = { id: 123 };
      jest.spyOn(deviceModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceModelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOtherDeviceType', () => {
      it('Should forward to otherDeviceTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(otherDeviceTypeService, 'compareOtherDeviceType');
        comp.compareOtherDeviceType(entity, entity2);
        expect(otherDeviceTypeService.compareOtherDeviceType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVendor', () => {
      it('Should forward to vendorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vendorService, 'compareVendor');
        comp.compareVendor(entity, entity2);
        expect(vendorService.compareVendor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOption', () => {
      it('Should forward to optionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(optionService, 'compareOption');
        comp.compareOption(entity, entity2);
        expect(optionService.compareOption).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
