import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OnlineBankingSharedModule } from '../../shared';
import {
    VendorService,
    VendorPopupService,
    VendorComponent,
    VendorDetailComponent,
    VendorDialogComponent,
    VendorPopupComponent,
    VendorDeletePopupComponent,
    VendorDeleteDialogComponent,
    vendorRoute,
    vendorPopupRoute,
    VendorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...vendorRoute,
    ...vendorPopupRoute,
];

@NgModule({
    imports: [
        OnlineBankingSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VendorComponent,
        VendorDetailComponent,
        VendorDialogComponent,
        VendorDeleteDialogComponent,
        VendorPopupComponent,
        VendorDeletePopupComponent,
    ],
    entryComponents: [
        VendorComponent,
        VendorDialogComponent,
        VendorPopupComponent,
        VendorDeleteDialogComponent,
        VendorDeletePopupComponent,
    ],
    providers: [
        VendorService,
        VendorPopupService,
        VendorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OnlineBankingVendorModule {}
