import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { OtherDeviceTypeService } from '../service/other-device-type.service';
import { IOtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeFormService } from './other-device-type-form.service';

import { OtherDeviceTypeUpdateComponent } from './other-device-type-update.component';

describe('OtherDeviceType Management Update Component', () => {
  let comp: OtherDeviceTypeUpdateComponent;
  let fixture: ComponentFixture<OtherDeviceTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let otherDeviceTypeFormService: OtherDeviceTypeFormService;
  let otherDeviceTypeService: OtherDeviceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OtherDeviceTypeUpdateComponent],
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
      .overrideTemplate(OtherDeviceTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OtherDeviceTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    otherDeviceTypeFormService = TestBed.inject(OtherDeviceTypeFormService);
    otherDeviceTypeService = TestBed.inject(OtherDeviceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const otherDeviceType: IOtherDeviceType = { id: 456 };

      activatedRoute.data = of({ otherDeviceType });
      comp.ngOnInit();

      expect(comp.otherDeviceType).toEqual(otherDeviceType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOtherDeviceType>>();
      const otherDeviceType = { id: 123 };
      jest.spyOn(otherDeviceTypeFormService, 'getOtherDeviceType').mockReturnValue(otherDeviceType);
      jest.spyOn(otherDeviceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ otherDeviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: otherDeviceType }));
      saveSubject.complete();

      // THEN
      expect(otherDeviceTypeFormService.getOtherDeviceType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(otherDeviceTypeService.update).toHaveBeenCalledWith(expect.objectContaining(otherDeviceType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOtherDeviceType>>();
      const otherDeviceType = { id: 123 };
      jest.spyOn(otherDeviceTypeFormService, 'getOtherDeviceType').mockReturnValue({ id: null });
      jest.spyOn(otherDeviceTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ otherDeviceType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: otherDeviceType }));
      saveSubject.complete();

      // THEN
      expect(otherDeviceTypeFormService.getOtherDeviceType).toHaveBeenCalled();
      expect(otherDeviceTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOtherDeviceType>>();
      const otherDeviceType = { id: 123 };
      jest.spyOn(otherDeviceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ otherDeviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(otherDeviceTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
