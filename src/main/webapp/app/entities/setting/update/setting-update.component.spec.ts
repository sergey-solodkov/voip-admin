import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { OptionValueService } from 'app/entities/option-value/service/option-value.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { ISetting } from '../setting.model';
import { SettingService } from '../service/setting.service';
import { SettingFormService } from './setting-form.service';

import { SettingUpdateComponent } from './setting-update.component';

describe('Setting Management Update Component', () => {
  let comp: SettingUpdateComponent;
  let fixture: ComponentFixture<SettingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let settingFormService: SettingFormService;
  let settingService: SettingService;
  let optionService: OptionService;
  let optionValueService: OptionValueService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SettingUpdateComponent],
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
      .overrideTemplate(SettingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    settingFormService = TestBed.inject(SettingFormService);
    settingService = TestBed.inject(SettingService);
    optionService = TestBed.inject(OptionService);
    optionValueService = TestBed.inject(OptionValueService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Option query and add missing value', () => {
      const setting: ISetting = { id: 456 };
      const option: IOption = { id: 8625 };
      setting.option = option;

      const optionCollection: IOption[] = [{ id: 9642 }];
      jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
      const additionalOptions = [option];
      const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
      jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setting });
      comp.ngOnInit();

      expect(optionService.query).toHaveBeenCalled();
      expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(
        optionCollection,
        ...additionalOptions.map(expect.objectContaining),
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OptionValue query and add missing value', () => {
      const setting: ISetting = { id: 456 };
      const selectedValues: IOptionValue[] = [{ id: 8436 }];
      setting.selectedValues = selectedValues;

      const optionValueCollection: IOptionValue[] = [{ id: 12882 }];
      jest.spyOn(optionValueService, 'query').mockReturnValue(of(new HttpResponse({ body: optionValueCollection })));
      const additionalOptionValues = [...selectedValues];
      const expectedCollection: IOptionValue[] = [...additionalOptionValues, ...optionValueCollection];
      jest.spyOn(optionValueService, 'addOptionValueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setting });
      comp.ngOnInit();

      expect(optionValueService.query).toHaveBeenCalled();
      expect(optionValueService.addOptionValueToCollectionIfMissing).toHaveBeenCalledWith(
        optionValueCollection,
        ...additionalOptionValues.map(expect.objectContaining),
      );
      expect(comp.optionValuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Device query and add missing value', () => {
      const setting: ISetting = { id: 456 };
      const device: IDevice = { id: 17011 };
      setting.device = device;

      const deviceCollection: IDevice[] = [{ id: 22320 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setting });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const setting: ISetting = { id: 456 };
      const option: IOption = { id: 13715 };
      setting.option = option;
      const selectedValues: IOptionValue = { id: 17145 };
      setting.selectedValues = [selectedValues];
      const device: IDevice = { id: 682 };
      setting.device = device;

      activatedRoute.data = of({ setting });
      comp.ngOnInit();

      expect(comp.optionsSharedCollection).toContain(option);
      expect(comp.optionValuesSharedCollection).toContain(selectedValues);
      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.setting).toEqual(setting);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISetting>>();
      const setting = { id: 123 };
      jest.spyOn(settingFormService, 'getSetting').mockReturnValue(setting);
      jest.spyOn(settingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setting }));
      saveSubject.complete();

      // THEN
      expect(settingFormService.getSetting).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(settingService.update).toHaveBeenCalledWith(expect.objectContaining(setting));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISetting>>();
      const setting = { id: 123 };
      jest.spyOn(settingFormService, 'getSetting').mockReturnValue({ id: null });
      jest.spyOn(settingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setting: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setting }));
      saveSubject.complete();

      // THEN
      expect(settingFormService.getSetting).toHaveBeenCalled();
      expect(settingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISetting>>();
      const setting = { id: 123 };
      jest.spyOn(settingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(settingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOption', () => {
      it('Should forward to optionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(optionService, 'compareOption');
        comp.compareOption(entity, entity2);
        expect(optionService.compareOption).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOptionValue', () => {
      it('Should forward to optionValueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(optionValueService, 'compareOptionValue');
        comp.compareOptionValue(entity, entity2);
        expect(optionValueService.compareOptionValue).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDevice', () => {
      it('Should forward to deviceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deviceService, 'compareDevice');
        comp.compareDevice(entity, entity2);
        expect(deviceService.compareDevice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
