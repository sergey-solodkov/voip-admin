import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../voip-account.test-samples';

import { VoipAccountFormService } from './voip-account-form.service';

describe('VoipAccount Form Service', () => {
  let service: VoipAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoipAccountFormService);
  });

  describe('Service methods', () => {
    describe('createVoipAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVoipAccountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            manual: expect.any(Object),
            username: expect.any(Object),
            passwordHash: expect.any(Object),
            sipServer: expect.any(Object),
            sipPort: expect.any(Object),
            lineEnable: expect.any(Object),
            lineNumber: expect.any(Object),
            device: expect.any(Object),
          }),
        );
      });

      it('passing IVoipAccount should create a new form with FormGroup', () => {
        const formGroup = service.createVoipAccountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            manual: expect.any(Object),
            username: expect.any(Object),
            passwordHash: expect.any(Object),
            sipServer: expect.any(Object),
            sipPort: expect.any(Object),
            lineEnable: expect.any(Object),
            lineNumber: expect.any(Object),
            device: expect.any(Object),
          }),
        );
      });
    });

    describe('getVoipAccount', () => {
      it('should return NewVoipAccount for default VoipAccount initial value', () => {
        const formGroup = service.createVoipAccountFormGroup(sampleWithNewData);

        const voipAccount = service.getVoipAccount(formGroup) as any;

        expect(voipAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewVoipAccount for empty VoipAccount initial value', () => {
        const formGroup = service.createVoipAccountFormGroup();

        const voipAccount = service.getVoipAccount(formGroup) as any;

        expect(voipAccount).toMatchObject({});
      });

      it('should return IVoipAccount', () => {
        const formGroup = service.createVoipAccountFormGroup(sampleWithRequiredData);

        const voipAccount = service.getVoipAccount(formGroup) as any;

        expect(voipAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVoipAccount should not enable id FormControl', () => {
        const formGroup = service.createVoipAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVoipAccount should disable id FormControl', () => {
        const formGroup = service.createVoipAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
