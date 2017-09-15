import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BillPaymentComponent } from './bill-payment.component';
import { BillPaymentDetailComponent } from './bill-payment-detail.component';
import { BillPaymentPopupComponent } from './bill-payment-dialog.component';
import { BillPaymentDeletePopupComponent } from './bill-payment-delete-dialog.component';

@Injectable()
export class BillPaymentResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const billPaymentRoute: Routes = [
    {
        path: 'bill-payment',
        component: BillPaymentComponent,
        resolve: {
            'pagingParams': BillPaymentResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.billPayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bill-payment/:id',
        component: BillPaymentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.billPayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billPaymentPopupRoute: Routes = [
    {
        path: 'bill-payment-new',
        component: BillPaymentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.billPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bill-payment/:id/edit',
        component: BillPaymentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.billPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bill-payment/:id/delete',
        component: BillPaymentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.billPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
