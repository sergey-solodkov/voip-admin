import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVoipAccount } from '../voip-account.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../voip-account.test-samples';

import { VoipAccountService } from './voip-account.service';

const requireRestSample: IVoipAccount = {
  ...sampleWithRequiredData,
};

describe('VoipAccount Service', () => {
  let service: VoipAccountService;
  let httpMock: HttpTestingController;
  let expectedResult: IVoipAccount | IVoipAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VoipAccountService);
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

    it('should create a VoipAccount', () => {
      const voipAccount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(voipAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VoipAccount', () => {
      const voipAccount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(voipAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VoipAccount', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VoipAccount', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VoipAccount', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVoipAccountToCollectionIfMissing', () => {
      it('should add a VoipAccount to an empty array', () => {
        const voipAccount: IVoipAccount = sampleWithRequiredData;
        expectedResult = service.addVoipAccountToCollectionIfMissing([], voipAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(voipAccount);
      });

      it('should not add a VoipAccount to an array that contains it', () => {
        const voipAccount: IVoipAccount = sampleWithRequiredData;
        const voipAccountCollection: IVoipAccount[] = [
          {
            ...voipAccount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, voipAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VoipAccount to an array that doesn't contain it", () => {
        const voipAccount: IVoipAccount = sampleWithRequiredData;
        const voipAccountCollection: IVoipAccount[] = [sampleWithPartialData];
        expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, voipAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(voipAccount);
      });

      it('should add only unique VoipAccount to an array', () => {
        const voipAccountArray: IVoipAccount[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const voipAccountCollection: IVoipAccount[] = [sampleWithRequiredData];
        expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, ...voipAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const voipAccount: IVoipAccount = sampleWithRequiredData;
        const voipAccount2: IVoipAccount = sampleWithPartialData;
        expectedResult = service.addVoipAccountToCollectionIfMissing([], voipAccount, voipAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(voipAccount);
        expect(expectedResult).toContain(voipAccount2);
      });

      it('should accept null and undefined values', () => {
        const voipAccount: IVoipAccount = sampleWithRequiredData;
        expectedResult = service.addVoipAccountToCollectionIfMissing([], null, voipAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(voipAccount);
      });

      it('should return initial array if no VoipAccount is added', () => {
        const voipAccountCollection: IVoipAccount[] = [sampleWithRequiredData];
        expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, undefined, null);
        expect(expectedResult).toEqual(voipAccountCollection);
      });
    });

    describe('compareVoipAccount', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVoipAccount(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVoipAccount(entity1, entity2);
        const compareResult2 = service.compareVoipAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVoipAccount(entity1, entity2);
        const compareResult2 = service.compareVoipAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVoipAccount(entity1, entity2);
        const compareResult2 = service.compareVoipAccount(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
