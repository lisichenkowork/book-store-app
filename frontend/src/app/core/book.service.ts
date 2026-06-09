import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Book, BookPayload, Page } from './models';

@Injectable({ providedIn: 'root' })
export class BookService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/books';

  list(page = 0, size = 12): Observable<Page<Book>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Book>>(this.base, { params });
  }

  search(term: string, page = 0, size = 12): Observable<Page<Book>> {
    let params = new HttpParams().set('page', page).set('size', size);
    // Backend search filters by exact author/title; send the term to both.
    params = params.set('titles', term).set('authors', term);
    return this.http.get<Page<Book>>(`${this.base}/search`, { params });
  }

  get(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.base}/${id}`);
  }

  create(payload: BookPayload): Observable<Book> {
    return this.http.post<Book>(this.base, payload);
  }

  update(id: number, payload: BookPayload): Observable<Book> {
    return this.http.put<Book>(`${this.base}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
