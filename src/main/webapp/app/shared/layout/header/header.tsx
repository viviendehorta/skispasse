import '../menu-bar.scss';

import React, {useState} from 'react';
import {Storage} from 'react-jhipster';
import {Button} from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import {Brand, Home, MapMenu} from './header-components';
import {AccountMenu, AdminMenu, EntitiesMenu, LocaleMenu} from '../menus';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  isInProduction: boolean;
  isSwaggerEnabled: boolean;
  currentLocale: string;
  onLocaleChange: Function;
}

const Header = (props: IHeaderProps) => {

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
    <div className="app-header">
      <LoadingBar className="loading-bar"/>

      <div className={isCollapsed ? "header-bar active" : "header-bar"} id="header-bar">

        <div className="header-bar-without-toggle-btn">

          <Brand/>

          <ul className="nav flex-column bg-white mb-0">

            <Home/>

            <MapMenu/>

            <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />

            <AccountMenu isAuthenticated={props.isAuthenticated} />

            {props.isAuthenticated && <EntitiesMenu />}

            {props.isAuthenticated && props.isAdmin && <AdminMenu showSwagger={props.isSwaggerEnabled} />}

          </ul>

        </div>

        <Button id="headerCollapseBtn" className="header-collapse-btn" onClick={toggleMenu}>
          <FontAwesomeIcon icon="bars" className="header-collapse-icon"/>
        </Button>

      </div>
    </div>
  );
};

export default Header;
