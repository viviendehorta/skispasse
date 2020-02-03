import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale.reducer';
import authentication, { AuthenticationState } from './authentication.reducer';
import applicationProfile, { ApplicationProfileState } from './application-profile.reducer';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import newsFactsBlobState, { NewsFactslobState } from './news-facts-blob.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly newsFactsBlobState: NewsFactslobState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  newsFactsBlobState,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
