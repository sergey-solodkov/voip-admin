import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../other-device-type.test-samples';

import { OtherDeviceTypeFormService } from './other-device-type-form.service';

describe('OtherDeviceType Form Service', () => {
  let service: OtherDeviceTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OtherDeviceTypeFormService);
  });

  describe('Service methods', () => {
    describe('createOtherDeviceTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IOtherDeviceType should create a new form with FormGroup', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getOtherDeviceType', () => {
      it('should return NewOtherDeviceType for default OtherDeviceType initial value', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup(sampleWithNewData);

        const otherDeviceType = service.getOtherDeviceType(formGroup) as any;

        expect(otherDeviceType).toMatchObject(sampleWithNewData);
      });

      it('should return NewOtherDeviceType for empty OtherDeviceType initial value', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup();

        const otherDeviceType = service.getOtherDeviceType(formGroup) as any;

        expect(otherDeviceType).toMatchObject({});
      });

      it('should return IOtherDeviceType', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup(sampleWithRequiredData);

        const otherDeviceType = service.getOtherDeviceType(formGroup) as any;

        expect(otherDeviceType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOtherDeviceType should not enable id FormControl', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOtherDeviceType should disable id FormControl', () => {
        const formGroup = service.createOtherDeviceTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
