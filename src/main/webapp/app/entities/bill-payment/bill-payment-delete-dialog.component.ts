import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BillPayment } from './bill-payment.model';
import { BillPaymentPopupService } from './bill-payment-popup.service';
import { BillPaymentService } from './bill-payment.service';

@Component({
    selector: 'jhi-bill-payment-delete-dialog',
    templateUrl: './bill-payment-delete-dialog.component.html'
})
export class BillPaymentDeleteDialogComponent {

    billPayment: BillPayment;

    constructor(
        private billPaymentService: BillPaymentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billPaymentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'billPaymentListModification',
                content: 'Deleted an billPayment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bill-payment-delete-popup',
    template: ''
})
export class BillPaymentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billPaymentPopupService: BillPaymentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.billPaymentPopupService
                .open(BillPaymentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
