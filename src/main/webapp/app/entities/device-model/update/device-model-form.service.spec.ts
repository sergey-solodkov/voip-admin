import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../device-model.test-samples';

import { DeviceModelFormService } from './device-model-form.service';

describe('DeviceModel Form Service', () => {
  let service: DeviceModelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceModelFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceModelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceModelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            configurable: expect.any(Object),
            linesAmount: expect.any(Object),
            deviceType: expect.any(Object),
            otherDeviceType: expect.any(Object),
            vendor: expect.any(Object),
            options: expect.any(Object),
          }),
        );
      });

      it('passing IDeviceModel should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceModelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            configurable: expect.any(Object),
            linesAmount: expect.any(Object),
            deviceType: expect.any(Object),
            otherDeviceType: expect.any(Object),
            vendor: expect.any(Object),
            options: expect.any(Object),
          }),
        );
      });
    });

    describe('getDeviceModel', () => {
      it('should return NewDeviceModel for default DeviceModel initial value', () => {
        const formGroup = service.createDeviceModelFormGroup(sampleWithNewData);

        const deviceModel = service.getDeviceModel(formGroup) as any;

        expect(deviceModel).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeviceModel for empty DeviceModel initial value', () => {
        const formGroup = service.createDeviceModelFormGroup();

        const deviceModel = service.getDeviceModel(formGroup) as any;

        expect(deviceModel).toMatchObject({});
      });

      it('should return IDeviceModel', () => {
        const formGroup = service.createDeviceModelFormGroup(sampleWithRequiredData);

        const deviceModel = service.getDeviceModel(formGroup) as any;

        expect(deviceModel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeviceModel should not enable id FormControl', () => {
        const formGroup = service.createDeviceModelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeviceModel should disable id FormControl', () => {
        const formGroup = service.createDeviceModelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
