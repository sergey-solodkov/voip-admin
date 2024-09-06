import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../device.test-samples';

import { DeviceFormService } from './device-form.service';

describe('Device Form Service', () => {
  let service: DeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mac: expect.any(Object),
            inventoryId: expect.any(Object),
            location: expect.any(Object),
            hostname: expect.any(Object),
            webAccessLogin: expect.any(Object),
            webAccessPasswordHash: expect.any(Object),
            dhcpEnabled: expect.any(Object),
            ipAddress: expect.any(Object),
            subnetMask: expect.any(Object),
            defaultGw: expect.any(Object),
            dns1: expect.any(Object),
            dns2: expect.any(Object),
            provisioningMode: expect.any(Object),
            provisioningUrl: expect.any(Object),
            ntp: expect.any(Object),
            configPath: expect.any(Object),
            notes: expect.any(Object),
            model: expect.any(Object),
            owner: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IDevice should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mac: expect.any(Object),
            inventoryId: expect.any(Object),
            location: expect.any(Object),
            hostname: expect.any(Object),
            webAccessLogin: expect.any(Object),
            webAccessPasswordHash: expect.any(Object),
            dhcpEnabled: expect.any(Object),
            ipAddress: expect.any(Object),
            subnetMask: expect.any(Object),
            defaultGw: expect.any(Object),
            dns1: expect.any(Object),
            dns2: expect.any(Object),
            provisioningMode: expect.any(Object),
            provisioningUrl: expect.any(Object),
            ntp: expect.any(Object),
            configPath: expect.any(Object),
            notes: expect.any(Object),
            model: expect.any(Object),
            owner: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getDevice', () => {
      it('should return NewDevice for default Device initial value', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithNewData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithNewData);
      });

      it('should return NewDevice for empty Device initial value', () => {
        const formGroup = service.createDeviceFormGroup();

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject({});
      });

      it('should return IDevice', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDevice should not enable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDevice should disable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
