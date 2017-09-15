import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AccountUserComponent } from './account-user.component';
import { AccountUserDetailComponent } from './account-user-detail.component';
import { AccountUserPopupComponent } from './account-user-dialog.component';
import { AccountUserDeletePopupComponent } from './account-user-delete-dialog.component';

export const accountUserRoute: Routes = [
    {
        path: 'account-user',
        component: AccountUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.accountUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'account-user/:id',
        component: AccountUserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.accountUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const accountUserPopupRoute: Routes = [
    {
        path: 'account-user-new',
        component: AccountUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.accountUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'account-user/:id/edit',
        component: AccountUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.accountUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'account-user/:id/delete',
        component: AccountUserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'onlineBankingApp.accountUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
