import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BillPayment } from './bill-payment.model';
import { BillPaymentPopupService } from './bill-payment-popup.service';
import { BillPaymentService } from './bill-payment.service';
import { Vendor, VendorService } from '../vendor';
import { AccountUser, AccountUserService } from '../account-user';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-bill-payment-dialog',
    templateUrl: './bill-payment-dialog.component.html'
})
export class BillPaymentDialogComponent implements OnInit {

    billPayment: BillPayment;
    isSaving: boolean;

    vendors: Vendor[];

    accountusers: AccountUser[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private billPaymentService: BillPaymentService,
        private vendorService: VendorService,
        private accountUserService: AccountUserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.vendorService.query()
            .subscribe((res: ResponseWrapper) => { this.vendors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.accountUserService.query()
            .subscribe((res: ResponseWrapper) => { this.accountusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.billPayment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.billPaymentService.update(this.billPayment));
        } else {
            this.subscribeToSaveResponse(
                this.billPaymentService.create(this.billPayment));
        }
    }

    private subscribeToSaveResponse(result: Observable<BillPayment>) {
        result.subscribe((res: BillPayment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: BillPayment) {
        this.eventManager.broadcast({ name: 'billPaymentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackVendorById(index: number, item: Vendor) {
        return item.id;
    }

    trackAccountUserById(index: number, item: AccountUser) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bill-payment-popup',
    template: ''
})
export class BillPaymentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billPaymentPopupService: BillPaymentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.billPaymentPopupService
                    .open(BillPaymentDialogComponent as Component, params['id']);
            } else {
                this.billPaymentPopupService
                    .open(BillPaymentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
