import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OnlineBankingSharedModule } from '../../shared';
import {
    BillPaymentService,
    BillPaymentPopupService,
    BillPaymentComponent,
    BillPaymentDetailComponent,
    BillPaymentDialogComponent,
    BillPaymentPopupComponent,
    BillPaymentDeletePopupComponent,
    BillPaymentDeleteDialogComponent,
    billPaymentRoute,
    billPaymentPopupRoute,
    BillPaymentResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...billPaymentRoute,
    ...billPaymentPopupRoute,
];

@NgModule({
    imports: [
        OnlineBankingSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BillPaymentComponent,
        BillPaymentDetailComponent,
        BillPaymentDialogComponent,
        BillPaymentDeleteDialogComponent,
        BillPaymentPopupComponent,
        BillPaymentDeletePopupComponent,
    ],
    entryComponents: [
        BillPaymentComponent,
        BillPaymentDialogComponent,
        BillPaymentPopupComponent,
        BillPaymentDeleteDialogComponent,
        BillPaymentDeletePopupComponent,
    ],
    providers: [
        BillPaymentService,
        BillPaymentPopupService,
        BillPaymentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OnlineBankingBillPaymentModule {}
