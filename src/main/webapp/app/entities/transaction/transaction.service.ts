import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Transaction } from './transaction.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TransactionService {

    private resourceUrl = 'api/transactions';

    constructor(private http: Http) { }

    create(transaction: Transaction): Observable<Transaction> {
        const copy = this.convert(transaction);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(transaction: Transaction): Observable<Transaction> {
        const copy = this.convert(transaction);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Transaction> {
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

    private convert(transaction: Transaction): Transaction {
        const copy: Transaction = Object.assign({}, transaction);
        return copy;
    }
}
