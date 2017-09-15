import { BaseEntity } from './../../shared';

export class Vendor implements BaseEntity {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public companyName?: string,
        public isRegistered?: boolean,
        public expiringDate?: string,
    ) {
        this.isRegistered = false;
    }
}
