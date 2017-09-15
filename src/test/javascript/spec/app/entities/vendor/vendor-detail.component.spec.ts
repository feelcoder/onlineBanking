/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { OnlineBankingTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VendorDetailComponent } from '../../../../../../main/webapp/app/entities/vendor/vendor-detail.component';
import { VendorService } from '../../../../../../main/webapp/app/entities/vendor/vendor.service';
import { Vendor } from '../../../../../../main/webapp/app/entities/vendor/vendor.model';

describe('Component Tests', () => {

    describe('Vendor Management Detail Component', () => {
        let comp: VendorDetailComponent;
        let fixture: ComponentFixture<VendorDetailComponent>;
        let service: VendorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OnlineBankingTestModule],
                declarations: [VendorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VendorService,
                    JhiEventManager
                ]
            }).overrideTemplate(VendorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VendorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VendorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Vendor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.vendor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
