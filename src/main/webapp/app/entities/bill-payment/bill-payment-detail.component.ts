import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { BillPayment } from './bill-payment.model';
import { BillPaymentService } from './bill-payment.service';

@Component({
    selector: 'jhi-bill-payment-detail',
    templateUrl: './bill-payment-detail.component.html'
})
export class BillPaymentDetailComponent implements OnInit, OnDestroy {

    billPayment: BillPayment;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private billPaymentService: BillPaymentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBillPayments();
    }

    load(id) {
        this.billPaymentService.find(id).subscribe((billPayment) => {
            this.billPayment = billPayment;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBillPayments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'billPaymentListModification',
            (response) => this.load(this.billPayment.id)
        );
    }
}
