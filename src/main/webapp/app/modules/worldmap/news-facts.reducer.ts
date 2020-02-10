import axios from 'axios';

import { FAILURE, REQUEST, SUCCESS } from 'app/utils/action-type.util';

export const ACTION_TYPES = {
  FETCH_ALL_NEWS_FACTS: 'news-facts/FETCH_ALL_NEWS_FACTS',
  GET_NEWS_FACT_DETAIL: 'news-facts/GET_NEWS_FACT_DETAIL'
};

const initialState = {
  allNewsFacts: null,
  currentNewsFactDetail: null,
  isFetching: false,
  errorMessage: null
};

export type NewsFactsState = Readonly<typeof initialState>;

export default (state: NewsFactsState = initialState, action): NewsFactsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ALL_NEWS_FACTS):
      return {
        ...state,
        isFetching: true,
        errorMessage: null,
        allNewsFacts: null
      };
    case FAILURE(ACTION_TYPES.FETCH_ALL_NEWS_FACTS):
      return {
        ...state,
        errorMessage: action.payload,
        isFetching: false,
        allNewsFacts: null
      };
    case SUCCESS(ACTION_TYPES.FETCH_ALL_NEWS_FACTS):
      return {
        ...state,
        allNewsFacts: action.payload.data,
        isFetching: false,
        errorMessage: null
      };
    case REQUEST(ACTION_TYPES.GET_NEWS_FACT_DETAIL):
      return {
        ...state,
        isFetching: true,
        errorMessage: null,
        currentNewsFactDetail: null
      };
    case FAILURE(ACTION_TYPES.GET_NEWS_FACT_DETAIL):
      return {
        ...state,
        errorMessage: action.payload,
        isFetching: false,
        currentNewsFactDetail: null
      };
    case SUCCESS(ACTION_TYPES.GET_NEWS_FACT_DETAIL):
      return {
        ...state,
        currentNewsFactDetail: action.payload.data,
        isFetching: false,
        errorMessage: null
      };
    default:
      return state;
  }
};

// Actions

export const fetchAllNewsFacts = () => ({
  type: ACTION_TYPES.FETCH_ALL_NEWS_FACTS,
  payload: axios.post('newsFacts/all')
});

export const getNewsFactDetail = (newsFactId: number) => ({
  type: ACTION_TYPES.GET_NEWS_FACT_DETAIL,
  payload: axios.post('newsFacts/' + newsFactId)
});
