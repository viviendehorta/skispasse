import React from 'react';

import {DropdownMenu, DropdownToggle, UncontrolledDropdown} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';


export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo"/>
  </div>
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
