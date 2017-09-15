/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { OnlineBankingTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BillPaymentDetailComponent } from '../../../../../../main/webapp/app/entities/bill-payment/bill-payment-detail.component';
import { BillPaymentService } from '../../../../../../main/webapp/app/entities/bill-payment/bill-payment.service';
import { BillPayment } from '../../../../../../main/webapp/app/entities/bill-payment/bill-payment.model';

describe('Component Tests', () => {

    describe('BillPayment Management Detail Component', () => {
        let comp: BillPaymentDetailComponent;
        let fixture: ComponentFixture<BillPaymentDetailComponent>;
        let service: BillPaymentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OnlineBankingTestModule],
                declarations: [BillPaymentDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BillPaymentService,
                    JhiEventManager
                ]
            }).overrideTemplate(BillPaymentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BillPaymentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillPaymentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BillPayment(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.billPayment).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
