import axios from 'axios';

import { SUCCESS } from 'app/utils/action-type.util';

export const ACTION_TYPES = {
  GET_PROFILE: 'applicationProfile/GET_PROFILE'
};

const initialState = {
  inProduction: true,
  isSwaggerEnabled: false
};

export type ApplicationProfileState = Readonly<typeof initialState>;

export default (state: ApplicationProfileState = initialState, action): ApplicationProfileState => {
  switch (action.type) {
    case SUCCESS(ACTION_TYPES.GET_PROFILE): {
      const { data } = action.payload;
      return {
        ...state,
        inProduction: data.activeProfiles.includes('prod'),
        isSwaggerEnabled: data.activeProfiles.includes('swagger')
      };
    }
    default:
      return state;
  }
};

export const getProfile = () => ({
  type: ACTION_TYPES.GET_PROFILE,
  payload: axios.get('management/info')
});
