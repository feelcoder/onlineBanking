import { BaseEntity } from './../../shared';

export class BillPayment implements BaseEntity {
    constructor(
        public id?: number,
        public scheduledDate?: string,
        public frequencyMode?: string,
        public paymentComplete?: boolean,
        public amount?: number,
        public vendorId?: number,
        public accountUserId?: number,
    ) {
        this.paymentComplete = false;
    }
}
