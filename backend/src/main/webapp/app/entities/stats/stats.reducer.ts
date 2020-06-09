import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStats, defaultValue } from 'app/shared/model/stats.model';

export const ACTION_TYPES = {
  FETCH_STATS_LIST: 'stats/FETCH_STATS_LIST',
  FETCH_STATS: 'stats/FETCH_STATS',
  CREATE_STATS: 'stats/CREATE_STATS',
  UPDATE_STATS: 'stats/UPDATE_STATS',
  DELETE_STATS: 'stats/DELETE_STATS',
  RESET: 'stats/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStats>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type StatsState = Readonly<typeof initialState>;

// Reducer

export default (state: StatsState = initialState, action): StatsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STATS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STATS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STATS):
    case REQUEST(ACTION_TYPES.UPDATE_STATS):
    case REQUEST(ACTION_TYPES.DELETE_STATS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STATS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STATS):
    case FAILURE(ACTION_TYPES.CREATE_STATS):
    case FAILURE(ACTION_TYPES.UPDATE_STATS):
    case FAILURE(ACTION_TYPES.DELETE_STATS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STATS):
    case SUCCESS(ACTION_TYPES.UPDATE_STATS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STATS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/stats';

// Actions

export const getEntities: ICrudGetAllAction<IStats> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_STATS_LIST,
  payload: axios.get<IStats>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IStats> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STATS,
    payload: axios.get<IStats>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStats> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STATS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStats> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STATS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStats> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STATS,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
