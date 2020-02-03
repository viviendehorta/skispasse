import React from 'react';

import {DropdownMenu, DropdownToggle, NavItem, NavLink, UncontrolledDropdown} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {Translate} from 'react-jhipster';
import {NavLink as Link} from 'react-router-dom';

import appConfig from 'app/config/constants';


export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo"/>
  </div>
);

export const Brand = props => (
  <NavLink tag={Link} to="/" className="brand-title menu-nav-item">
    <span>
      <Translate contentKey="global.title">Skispasse</Translate>
    </span>
    <span className="navbar-version">{appConfig.VERSION}</span>
  </NavLink>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="menu-nav-item">
      <span>
        <FontAwesomeIcon icon="home"></FontAwesomeIcon>
      </span>
      <Translate contentKey="global.menu.home">Home</Translate>
    </NavLink>
  </NavItem>
);

export const MapMenu = props => (
  <NavItem>
    <NavLink tag={Link} to="/worldmap" className="menu-nav-item">
      <FontAwesomeIcon icon="map"/>
      <span>
        <Translate contentKey="global.menu.worldmap">Map</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id}>
    <DropdownToggle nav caret className="menu-nav-item d-flex align-items-center">
      <FontAwesomeIcon icon={props.icon}/>
      <span>{props.name}</span>
    </DropdownToggle>
    <DropdownMenu right style={props.style}>
      {props.children}
    </DropdownMenu>
  </UncontrolledDropdown>
);
