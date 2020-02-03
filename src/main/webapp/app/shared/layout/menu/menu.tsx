import './menu.scss';

import React, {useState} from 'react';
import {Storage} from 'react-jhipster';
import {Button} from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {Brand, Home, MapMenu} from './menu-components';
import {AccountMenu, AdminMenu, EntitiesMenu, LocaleMenu} from './';

export interface IMenuProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  isInProduction: boolean;
  isSwaggerEnabled: boolean;
  currentLocale: string;
  onLocaleChange: Function;
}

const Menu = (props: IMenuProps) => {

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    props.onLocaleChange(langKey);
  };

  const [isCollapsed, setIsCollapsed] = useState(false);

  const toggleMenu = function () {
    setIsCollapsed(!isCollapsed);
  };

  return (
    <div className="app-menu">
      <LoadingBar className="loading-bar"/>

      <div className={isCollapsed ? "menu collapsed" : "menu"} id="menu">

        <div className="menu-without-toggle-btn">

          <Brand/>

          <ul className="nav flex-column bg-white mb-0">

            <Home/>

            <MapMenu/>

            <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange}/>

            <AccountMenu isAuthenticated={props.isAuthenticated}/>

            {props.isAuthenticated && <EntitiesMenu/>}

            {props.isAuthenticated && props.isAdmin && <AdminMenu showSwagger={props.isSwaggerEnabled}/>}

          </ul>

        </div>

        <Button id="menuCollapseBtn" className="menu-collapse-btn" onClick={toggleMenu}>
          <FontAwesomeIcon icon="bars" className="menu-collapse-icon"/>
        </Button>

      </div>
    </div>
  );
};

export default Menu;
