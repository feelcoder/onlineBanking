import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { OnlineBankingAccountUserModule } from './account-user/account-user.module';
import { OnlineBankingTransactionModule } from './transaction/transaction.module';
import { OnlineBankingVendorModule } from './vendor/vendor.module';
import { OnlineBankingBillPaymentModule } from './bill-payment/bill-payment.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        OnlineBankingAccountUserModule,
        OnlineBankingTransactionModule,
        OnlineBankingVendorModule,
        OnlineBankingBillPaymentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OnlineBankingEntityModule {}
