import { BaseEntity } from './../../shared';

export class AccountUser implements BaseEntity {
    constructor(
        public id?: number,
        public accountType?: string,
        public accountNo?: string,
        public balance?: number,
        public limit?: number,
        public isActivated?: boolean,
        public createdDate?: any,
        public userId?: number,
    ) {
        this.isActivated = false;
    }
}
