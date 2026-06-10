import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Category, CategoryPayload, Page } from './models';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/categories';

  list(page = 0, size = 50): Observable<Page<Category>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Category>>(this.base, { params });
  }

  get(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.base}/${id}`);
  }

  create(payload: CategoryPayload): Observable<Category> {
    return this.http.post<Category>(this.base, payload);
  }

  update(id: number, payload: CategoryPayload): Observable<Category> {
    return this.http.put<Category>(`${this.base}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
