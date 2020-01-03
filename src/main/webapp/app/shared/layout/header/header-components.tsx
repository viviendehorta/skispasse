import React from 'react';
import {Translate} from 'react-jhipster';

import {NavItem, NavLink, NavbarBrand} from 'reactstrap';
import {NavLink as Link} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo"/>
  </div>
);

export const Brand = props => (
  <NavLink tag={Link} to="/" className="brand-title header-nav-item">
    <span>
      <Translate contentKey="global.title">Skispasse</Translate>
    </span>
    <span className="navbar-version">{appConfig.VERSION}</span>
  </NavLink>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="header-nav-item">
      <span>
        <FontAwesomeIcon icon="home"></FontAwesomeIcon>
      </span>
      <Translate contentKey="global.menu.home">Home</Translate>
    </NavLink>
  </NavItem>
);

export const MapMenu = props => (
  <NavItem>
    <NavLink tag={Link} to="/worldmap" className="header-nav-item">
      <FontAwesomeIcon icon="map"/>
      <span>
        <Translate contentKey="global.menu.wmap">Map</Translate>
      </span>
    </NavLink>
  </NavItem>
);
