import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AccountUser } from './account-user.model';
import { AccountUserPopupService } from './account-user-popup.service';
import { AccountUserService } from './account-user.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-account-user-dialog',
    templateUrl: './account-user-dialog.component.html'
})
export class AccountUserDialogComponent implements OnInit {

    accountUser: AccountUser;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private accountUserService: AccountUserService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.accountUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.accountUserService.update(this.accountUser));
        } else {
            this.subscribeToSaveResponse(
                this.accountUserService.create(this.accountUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<AccountUser>) {
        result.subscribe((res: AccountUser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AccountUser) {
        this.eventManager.broadcast({ name: 'accountUserListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-account-user-popup',
    template: ''
})
export class AccountUserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountUserPopupService: AccountUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.accountUserPopupService
                    .open(AccountUserDialogComponent as Component, params['id']);
            } else {
                this.accountUserPopupService
                    .open(AccountUserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
