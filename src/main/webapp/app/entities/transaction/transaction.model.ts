import { BaseEntity } from './../../shared';

export class Transaction implements BaseEntity {
    constructor(
        public id?: number,
        public type?: string,
        public description?: string,
        public date?: string,
        public amount?: number,
        public senderAccountId?: number,
        public receiverAccountId?: number,
    ) {
    }
}
