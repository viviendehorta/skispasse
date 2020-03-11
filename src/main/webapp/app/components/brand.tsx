import React from 'react';

import {NavLink} from 'reactstrap';
import {Translate} from 'react-jhipster';
import {NavLink as Link} from 'react-router-dom';

import appConfig from 'app/config/constants';

export const Brand = props => (
  <div className="brand-title">
    <span>
      <Translate contentKey="global.title">Skispasse</Translate>
    </span>
    <span className="navbar-version">{appConfig.VERSION}</span>
  </div>
);
