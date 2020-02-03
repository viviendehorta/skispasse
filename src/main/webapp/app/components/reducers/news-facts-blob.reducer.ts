import axios from 'axios';

import { FAILURE, REQUEST, SUCCESS } from 'app/components/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_NEWS_FACTS_OBJECT: 'administration/FETCH_NEWS_FACTS_OBJECT'
};

const initialState = {
  newsFactsBlob: null,
  isFetching: false,
  errorMessage: null
};

export type NewsFactslobState = Readonly<typeof initialState>;

export default (state: NewsFactslobState = initialState, action): NewsFactslobState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_NEWS_FACTS_OBJECT):
      return {
        ...state,
        isFetching: true,
        errorMessage: null,
        newsFactsBlob: null
      };
    case FAILURE(ACTION_TYPES.FETCH_NEWS_FACTS_OBJECT):
      return {
        ...state,
        errorMessage: action.payload,
        isFetching: false,
        newsFactsBlob: null
      };
    case SUCCESS(ACTION_TYPES.FETCH_NEWS_FACTS_OBJECT):
      return {
        ...state,
        newsFactsBlob: action.payload.data,
        isFetching: false,
        errorMessage: null
      };
    default:
      return state;
  }
};

export const fetchNewsFactsBlob = () => ({
  type: ACTION_TYPES.FETCH_NEWS_FACTS_OBJECT,
  payload: axios.post('newsFacts/all')
});
