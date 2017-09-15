import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OnlineBankingSharedModule } from '../../shared';
import { OnlineBankingAdminModule } from '../../admin/admin.module';
import {
    AccountUserService,
    AccountUserPopupService,
    AccountUserComponent,
    AccountUserDetailComponent,
    AccountUserDialogComponent,
    AccountUserPopupComponent,
    AccountUserDeletePopupComponent,
    AccountUserDeleteDialogComponent,
    accountUserRoute,
    accountUserPopupRoute,
} from './';

const ENTITY_STATES = [
    ...accountUserRoute,
    ...accountUserPopupRoute,
];

@NgModule({
    imports: [
        OnlineBankingSharedModule,
        OnlineBankingAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AccountUserComponent,
        AccountUserDetailComponent,
        AccountUserDialogComponent,
        AccountUserDeleteDialogComponent,
        AccountUserPopupComponent,
        AccountUserDeletePopupComponent,
    ],
    entryComponents: [
        AccountUserComponent,
        AccountUserDialogComponent,
        AccountUserPopupComponent,
        AccountUserDeleteDialogComponent,
        AccountUserDeletePopupComponent,
    ],
    providers: [
        AccountUserService,
        AccountUserPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OnlineBankingAccountUserModule {}
