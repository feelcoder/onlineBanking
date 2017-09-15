import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Vendor } from './vendor.model';
import { VendorService } from './vendor.service';

@Component({
    selector: 'jhi-vendor-detail',
    templateUrl: './vendor-detail.component.html'
})
export class VendorDetailComponent implements OnInit, OnDestroy {

    vendor: Vendor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private vendorService: VendorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVendors();
    }

    load(id) {
        this.vendorService.find(id).subscribe((vendor) => {
            this.vendor = vendor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVendors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vendorListModification',
            (response) => this.load(this.vendor.id)
        );
    }
}
