import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AccountUser } from './account-user.model';
import { AccountUserPopupService } from './account-user-popup.service';
import { AccountUserService } from './account-user.service';

@Component({
    selector: 'jhi-account-user-delete-dialog',
    templateUrl: './account-user-delete-dialog.component.html'
})
export class AccountUserDeleteDialogComponent {

    accountUser: AccountUser;

    constructor(
        private accountUserService: AccountUserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.accountUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'accountUserListModification',
                content: 'Deleted an accountUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-account-user-delete-popup',
    template: ''
})
export class AccountUserDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountUserPopupService: AccountUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.accountUserPopupService
                .open(AccountUserDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
