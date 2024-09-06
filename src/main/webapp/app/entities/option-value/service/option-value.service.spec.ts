import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOptionValue } from '../option-value.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../option-value.test-samples';

import { OptionValueService } from './option-value.service';

const requireRestSample: IOptionValue = {
  ...sampleWithRequiredData,
};

describe('OptionValue Service', () => {
  let service: OptionValueService;
  let httpMock: HttpTestingController;
  let expectedResult: IOptionValue | IOptionValue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OptionValueService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a OptionValue', () => {
      const optionValue = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(optionValue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OptionValue', () => {
      const optionValue = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(optionValue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OptionValue', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OptionValue', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OptionValue', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOptionValueToCollectionIfMissing', () => {
      it('should add a OptionValue to an empty array', () => {
        const optionValue: IOptionValue = sampleWithRequiredData;
        expectedResult = service.addOptionValueToCollectionIfMissing([], optionValue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(optionValue);
      });

      it('should not add a OptionValue to an array that contains it', () => {
        const optionValue: IOptionValue = sampleWithRequiredData;
        const optionValueCollection: IOptionValue[] = [
          {
            ...optionValue,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, optionValue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OptionValue to an array that doesn't contain it", () => {
        const optionValue: IOptionValue = sampleWithRequiredData;
        const optionValueCollection: IOptionValue[] = [sampleWithPartialData];
        expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, optionValue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(optionValue);
      });

      it('should add only unique OptionValue to an array', () => {
        const optionValueArray: IOptionValue[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const optionValueCollection: IOptionValue[] = [sampleWithRequiredData];
        expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, ...optionValueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const optionValue: IOptionValue = sampleWithRequiredData;
        const optionValue2: IOptionValue = sampleWithPartialData;
        expectedResult = service.addOptionValueToCollectionIfMissing([], optionValue, optionValue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(optionValue);
        expect(expectedResult).toContain(optionValue2);
      });

      it('should accept null and undefined values', () => {
        const optionValue: IOptionValue = sampleWithRequiredData;
        expectedResult = service.addOptionValueToCollectionIfMissing([], null, optionValue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(optionValue);
      });

      it('should return initial array if no OptionValue is added', () => {
        const optionValueCollection: IOptionValue[] = [sampleWithRequiredData];
        expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, undefined, null);
        expect(expectedResult).toEqual(optionValueCollection);
      });
    });

    describe('compareOptionValue', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOptionValue(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOptionValue(entity1, entity2);
        const compareResult2 = service.compareOptionValue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOptionValue(entity1, entity2);
        const compareResult2 = service.compareOptionValue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOptionValue(entity1, entity2);
        const compareResult2 = service.compareOptionValue(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
