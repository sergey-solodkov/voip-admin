import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDeviceModel } from '../device-model.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../device-model.test-samples';

import { DeviceModelService } from './device-model.service';

const requireRestSample: IDeviceModel = {
  ...sampleWithRequiredData,
};

describe('DeviceModel Service', () => {
  let service: DeviceModelService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeviceModel | IDeviceModel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DeviceModelService);
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

    it('should create a DeviceModel', () => {
      const deviceModel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deviceModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeviceModel', () => {
      const deviceModel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deviceModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeviceModel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeviceModel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeviceModel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeviceModelToCollectionIfMissing', () => {
      it('should add a DeviceModel to an empty array', () => {
        const deviceModel: IDeviceModel = sampleWithRequiredData;
        expectedResult = service.addDeviceModelToCollectionIfMissing([], deviceModel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceModel);
      });

      it('should not add a DeviceModel to an array that contains it', () => {
        const deviceModel: IDeviceModel = sampleWithRequiredData;
        const deviceModelCollection: IDeviceModel[] = [
          {
            ...deviceModel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeviceModelToCollectionIfMissing(deviceModelCollection, deviceModel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeviceModel to an array that doesn't contain it", () => {
        const deviceModel: IDeviceModel = sampleWithRequiredData;
        const deviceModelCollection: IDeviceModel[] = [sampleWithPartialData];
        expectedResult = service.addDeviceModelToCollectionIfMissing(deviceModelCollection, deviceModel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceModel);
      });

      it('should add only unique DeviceModel to an array', () => {
        const deviceModelArray: IDeviceModel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deviceModelCollection: IDeviceModel[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceModelToCollectionIfMissing(deviceModelCollection, ...deviceModelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deviceModel: IDeviceModel = sampleWithRequiredData;
        const deviceModel2: IDeviceModel = sampleWithPartialData;
        expectedResult = service.addDeviceModelToCollectionIfMissing([], deviceModel, deviceModel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceModel);
        expect(expectedResult).toContain(deviceModel2);
      });

      it('should accept null and undefined values', () => {
        const deviceModel: IDeviceModel = sampleWithRequiredData;
        expectedResult = service.addDeviceModelToCollectionIfMissing([], null, deviceModel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceModel);
      });

      it('should return initial array if no DeviceModel is added', () => {
        const deviceModelCollection: IDeviceModel[] = [sampleWithRequiredData];
        expectedResult = service.addDeviceModelToCollectionIfMissing(deviceModelCollection, undefined, null);
        expect(expectedResult).toEqual(deviceModelCollection);
      });
    });

    describe('compareDeviceModel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeviceModel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeviceModel(entity1, entity2);
        const compareResult2 = service.compareDeviceModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeviceModel(entity1, entity2);
        const compareResult2 = service.compareDeviceModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeviceModel(entity1, entity2);
        const compareResult2 = service.compareDeviceModel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
