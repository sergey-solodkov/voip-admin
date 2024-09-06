import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISetting } from 'app/entities/setting/setting.model';
import { SettingService } from 'app/entities/setting/service/setting.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IOptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';
import { OptionValueFormService } from './option-value-form.service';

import { OptionValueUpdateComponent } from './option-value-update.component';

describe('OptionValue Management Update Component', () => {
  let comp: OptionValueUpdateComponent;
  let fixture: ComponentFixture<OptionValueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionValueFormService: OptionValueFormService;
  let optionValueService: OptionValueService;
  let settingService: SettingService;
  let optionService: OptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OptionValueUpdateComponent],
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
      .overrideTemplate(OptionValueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OptionValueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    optionValueFormService = TestBed.inject(OptionValueFormService);
    optionValueService = TestBed.inject(OptionValueService);
    settingService = TestBed.inject(SettingService);
    optionService = TestBed.inject(OptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Setting query and add missing value', () => {
      const optionValue: IOptionValue = { id: 456 };
      const settings: ISetting[] = [{ id: 6543 }];
      optionValue.settings = settings;

      const settingCollection: ISetting[] = [{ id: 18717 }];
      jest.spyOn(settingService, 'query').mockReturnValue(of(new HttpResponse({ body: settingCollection })));
      const additionalSettings = [...settings];
      const expectedCollection: ISetting[] = [...additionalSettings, ...settingCollection];
      jest.spyOn(settingService, 'addSettingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ optionValue });
      comp.ngOnInit();

      expect(settingService.query).toHaveBeenCalled();
      expect(settingService.addSettingToCollectionIfMissing).toHaveBeenCalledWith(
        settingCollection,
        ...additionalSettings.map(expect.objectContaining),
      );
      expect(comp.settingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Option query and add missing value', () => {
      const optionValue: IOptionValue = { id: 456 };
      const option: IOption = { id: 13763 };
      optionValue.option = option;

      const optionCollection: IOption[] = [{ id: 14854 }];
      jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
      const additionalOptions = [option];
      const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
      jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ optionValue });
      comp.ngOnInit();

      expect(optionService.query).toHaveBeenCalled();
      expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(
        optionCollection,
        ...additionalOptions.map(expect.objectContaining),
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const optionValue: IOptionValue = { id: 456 };
      const settings: ISetting = { id: 7286 };
      optionValue.settings = [settings];
      const option: IOption = { id: 16597 };
      optionValue.option = option;

      activatedRoute.data = of({ optionValue });
      comp.ngOnInit();

      expect(comp.settingsSharedCollection).toContain(settings);
      expect(comp.optionsSharedCollection).toContain(option);
      expect(comp.optionValue).toEqual(optionValue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptionValue>>();
      const optionValue = { id: 123 };
      jest.spyOn(optionValueFormService, 'getOptionValue').mockReturnValue(optionValue);
      jest.spyOn(optionValueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ optionValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: optionValue }));
      saveSubject.complete();

      // THEN
      expect(optionValueFormService.getOptionValue).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(optionValueService.update).toHaveBeenCalledWith(expect.objectContaining(optionValue));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptionValue>>();
      const optionValue = { id: 123 };
      jest.spyOn(optionValueFormService, 'getOptionValue').mockReturnValue({ id: null });
      jest.spyOn(optionValueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ optionValue: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: optionValue }));
      saveSubject.complete();

      // THEN
      expect(optionValueFormService.getOptionValue).toHaveBeenCalled();
      expect(optionValueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptionValue>>();
      const optionValue = { id: 123 };
      jest.spyOn(optionValueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ optionValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(optionValueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSetting', () => {
      it('Should forward to settingService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(settingService, 'compareSetting');
        comp.compareSetting(entity, entity2);
        expect(settingService.compareSetting).toHaveBeenCalledWith(entity, entity2);
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
