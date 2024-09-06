import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOtherDeviceType } from '../other-device-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../other-device-type.test-samples';

import { OtherDeviceTypeService } from './other-device-type.service';

const requireRestSample: IOtherDeviceType = {
  ...sampleWithRequiredData,
};

describe('OtherDeviceType Service', () => {
  let service: OtherDeviceTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IOtherDeviceType | IOtherDeviceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OtherDeviceTypeService);
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

    it('should create a OtherDeviceType', () => {
      const otherDeviceType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(otherDeviceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OtherDeviceType', () => {
      const otherDeviceType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(otherDeviceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OtherDeviceType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OtherDeviceType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OtherDeviceType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOtherDeviceTypeToCollectionIfMissing', () => {
      it('should add a OtherDeviceType to an empty array', () => {
        const otherDeviceType: IOtherDeviceType = sampleWithRequiredData;
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing([], otherDeviceType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(otherDeviceType);
      });

      it('should not add a OtherDeviceType to an array that contains it', () => {
        const otherDeviceType: IOtherDeviceType = sampleWithRequiredData;
        const otherDeviceTypeCollection: IOtherDeviceType[] = [
          {
            ...otherDeviceType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, otherDeviceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OtherDeviceType to an array that doesn't contain it", () => {
        const otherDeviceType: IOtherDeviceType = sampleWithRequiredData;
        const otherDeviceTypeCollection: IOtherDeviceType[] = [sampleWithPartialData];
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, otherDeviceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(otherDeviceType);
      });

      it('should add only unique OtherDeviceType to an array', () => {
        const otherDeviceTypeArray: IOtherDeviceType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const otherDeviceTypeCollection: IOtherDeviceType[] = [sampleWithRequiredData];
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, ...otherDeviceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const otherDeviceType: IOtherDeviceType = sampleWithRequiredData;
        const otherDeviceType2: IOtherDeviceType = sampleWithPartialData;
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing([], otherDeviceType, otherDeviceType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(otherDeviceType);
        expect(expectedResult).toContain(otherDeviceType2);
      });

      it('should accept null and undefined values', () => {
        const otherDeviceType: IOtherDeviceType = sampleWithRequiredData;
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing([], null, otherDeviceType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(otherDeviceType);
      });

      it('should return initial array if no OtherDeviceType is added', () => {
        const otherDeviceTypeCollection: IOtherDeviceType[] = [sampleWithRequiredData];
        expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(otherDeviceTypeCollection);
      });
    });

    describe('compareOtherDeviceType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOtherDeviceType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOtherDeviceType(entity1, entity2);
        const compareResult2 = service.compareOtherDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOtherDeviceType(entity1, entity2);
        const compareResult2 = service.compareOtherDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOtherDeviceType(entity1, entity2);
        const compareResult2 = service.compareOtherDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
