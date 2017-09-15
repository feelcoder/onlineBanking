import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { AccountUser } from './account-user.model';
import { AccountUserService } from './account-user.service';

@Component({
    selector: 'jhi-account-user-detail',
    templateUrl: './account-user-detail.component.html'
})
export class AccountUserDetailComponent implements OnInit, OnDestroy {

    accountUser: AccountUser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private accountUserService: AccountUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAccountUsers();
    }

    load(id) {
        this.accountUserService.find(id).subscribe((accountUser) => {
            this.accountUser = accountUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAccountUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'accountUserListModification',
            (response) => this.load(this.accountUser.id)
        );
    }
}
