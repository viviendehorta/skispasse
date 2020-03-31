import { Injectable } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-session.service';

@Injectable({ providedIn: 'root' })
export class NewsCategoryService {
  ALL_CATEGORIES: any[] = [
    {
      id: 1,
      label: 'Démonstration',
      value: '1',
      isSelected: false
    },
    {
      id: 2,
      label: 'Sport',
      value: '2',
      isSelected: false
    },
    {
      id: 3,
      label: 'Culture',
      value: '3',
      isSelected: false
    },
    {
      id: 4,
      label: 'Show',
      value: '4',
      isSelected: false
    },
    {
      id: 5,
      label: 'Nature',
      value: '5',
      isSelected: false
    },
    {
      id: 6,
      label: 'Other',
      value: '6',
      isSelected: false
    }
  ];

  constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider) {}

  getCategories() {
    return this.ALL_CATEGORIES;
  }

  getSelectedCategoryIds(): number[] {
    return this.ALL_CATEGORIES.filter(category => category.isSelected).map(category => category.id);
  }

  setCategorySelection(categoryValue: string, isSelected: boolean) {
    const category = this.ALL_CATEGORIES.find(newsCategory => newsCategory.value === categoryValue);
    category.isSelected = isSelected;
  }
}
