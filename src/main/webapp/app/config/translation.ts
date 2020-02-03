import { TranslatorContext, Storage } from 'react-jhipster';

import { setLocale } from 'app/components/reducers/locale.reducer';

TranslatorContext.setDefaultLocale('fr');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const languages: any = {
  fr: { name: 'Français' }
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'fr')));
};
