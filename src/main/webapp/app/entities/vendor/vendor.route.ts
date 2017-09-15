import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VendorComponent } from './vendor.component';
import { VendorDetailComponent } from './vendor-detail.component';
import { VendorPopupComponent } from './vendor-dialog.component';
import { VendorDeletePopupComponent } from './vendor-delete-dialog.component';

@Injectable()
export class VendorResolvePagingParams implements Resolve<any> {

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

export const vendorRoute: Routes = [
    {
        path: 'vendor',
        component: VendorComponent,
        resolve: {
            'pagingParams': VendorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.vendor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vendor/:id',
        component: VendorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.vendor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vendorPopupRoute: Routes = [
    {
        path: 'vendor-new',
        component: VendorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.vendor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vendor/:id/edit',
        component: VendorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.vendor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vendor/:id/delete',
        component: VendorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.vendor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
