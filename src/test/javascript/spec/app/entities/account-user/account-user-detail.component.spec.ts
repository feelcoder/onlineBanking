/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { OnlineBankingTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AccountUserDetailComponent } from '../../../../../../main/webapp/app/entities/account-user/account-user-detail.component';
import { AccountUserService } from '../../../../../../main/webapp/app/entities/account-user/account-user.service';
import { AccountUser } from '../../../../../../main/webapp/app/entities/account-user/account-user.model';

describe('Component Tests', () => {

    describe('AccountUser Management Detail Component', () => {
        let comp: AccountUserDetailComponent;
        let fixture: ComponentFixture<AccountUserDetailComponent>;
        let service: AccountUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OnlineBankingTestModule],
                declarations: [AccountUserDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AccountUserService,
                    JhiEventManager
                ]
            }).overrideTemplate(AccountUserDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AccountUserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AccountUserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AccountUser(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.accountUser).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
