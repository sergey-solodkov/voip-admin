import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { VoipAccountService } from '../service/voip-account.service';
import { IVoipAccount } from '../voip-account.model';
import { VoipAccountFormService } from './voip-account-form.service';

import { VoipAccountUpdateComponent } from './voip-account-update.component';

describe('VoipAccount Management Update Component', () => {
  let comp: VoipAccountUpdateComponent;
  let fixture: ComponentFixture<VoipAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let voipAccountFormService: VoipAccountFormService;
  let voipAccountService: VoipAccountService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VoipAccountUpdateComponent],
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
      .overrideTemplate(VoipAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VoipAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    voipAccountFormService = TestBed.inject(VoipAccountFormService);
    voipAccountService = TestBed.inject(VoipAccountService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const voipAccount: IVoipAccount = { id: 456 };
      const device: IDevice = { id: 20248 };
      voipAccount.device = device;

      const deviceCollection: IDevice[] = [{ id: 7885 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voipAccount });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const voipAccount: IVoipAccount = { id: 456 };
      const device: IDevice = { id: 19062 };
      voipAccount.device = device;

      activatedRoute.data = of({ voipAccount });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.voipAccount).toEqual(voipAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVoipAccount>>();
      const voipAccount = { id: 123 };
      jest.spyOn(voipAccountFormService, 'getVoipAccount').mockReturnValue(voipAccount);
      jest.spyOn(voipAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voipAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: voipAccount }));
      saveSubject.complete();

      // THEN
      expect(voipAccountFormService.getVoipAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(voipAccountService.update).toHaveBeenCalledWith(expect.objectContaining(voipAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVoipAccount>>();
      const voipAccount = { id: 123 };
      jest.spyOn(voipAccountFormService, 'getVoipAccount').mockReturnValue({ id: null });
      jest.spyOn(voipAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voipAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: voipAccount }));
      saveSubject.complete();

      // THEN
      expect(voipAccountFormService.getVoipAccount).toHaveBeenCalled();
      expect(voipAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVoipAccount>>();
      const voipAccount = { id: 123 };
      jest.spyOn(voipAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voipAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(voipAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
