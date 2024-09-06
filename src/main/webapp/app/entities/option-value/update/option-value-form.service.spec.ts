import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../option-value.test-samples';

import { OptionValueFormService } from './option-value-form.service';

describe('OptionValue Form Service', () => {
  let service: OptionValueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OptionValueFormService);
  });

  describe('Service methods', () => {
    describe('createOptionValueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOptionValueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            settings: expect.any(Object),
            option: expect.any(Object),
          }),
        );
      });

      it('passing IOptionValue should create a new form with FormGroup', () => {
        const formGroup = service.createOptionValueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            settings: expect.any(Object),
            option: expect.any(Object),
          }),
        );
      });
    });

    describe('getOptionValue', () => {
      it('should return NewOptionValue for default OptionValue initial value', () => {
        const formGroup = service.createOptionValueFormGroup(sampleWithNewData);

        const optionValue = service.getOptionValue(formGroup) as any;

        expect(optionValue).toMatchObject(sampleWithNewData);
      });

      it('should return NewOptionValue for empty OptionValue initial value', () => {
        const formGroup = service.createOptionValueFormGroup();

        const optionValue = service.getOptionValue(formGroup) as any;

        expect(optionValue).toMatchObject({});
      });

      it('should return IOptionValue', () => {
        const formGroup = service.createOptionValueFormGroup(sampleWithRequiredData);

        const optionValue = service.getOptionValue(formGroup) as any;

        expect(optionValue).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOptionValue should not enable id FormControl', () => {
        const formGroup = service.createOptionValueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOptionValue should disable id FormControl', () => {
        const formGroup = service.createOptionValueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
