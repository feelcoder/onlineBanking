import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { BillPayment } from './bill-payment.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BillPaymentService {

    private resourceUrl = 'api/bill-payments';

    constructor(private http: Http) { }

    create(billPayment: BillPayment): Observable<BillPayment> {
        const copy = this.convert(billPayment);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(billPayment: BillPayment): Observable<BillPayment> {
        const copy = this.convert(billPayment);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<BillPayment> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(billPayment: BillPayment): BillPayment {
        const copy: BillPayment = Object.assign({}, billPayment);
        return copy;
    }
}
