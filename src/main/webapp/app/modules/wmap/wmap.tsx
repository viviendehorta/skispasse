import './wmap.scss';

import React from 'react';
import {MapCmp} from "app/shared/map/map-cmp";
import {IRootState} from "app/shared/reducers";
import {systemHealth} from "app/modules/administration/administration.reducer";
import {connect} from "react-redux";

export interface IWMapPageProps extends StateProps, DispatchProps {}

export const WMapPage = () => {

  return (
    <MapCmp/>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
});

const mapDispatchToProps = { systemHealth };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WMapPage);
