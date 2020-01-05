import './vertical-collapse.scss'
import React, {useState} from 'react';
import {Button, Card, CardBody, CardTitle} from 'reactstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const VerticalCollapse = (props) => {

  const [isCollapsed, setIsCollapsed] = useState(false);

  const collapse = function () {
    setIsCollapsed(true);
  };

  const uncollapse = function () {
    setIsCollapsed(false);
  };

  return (
    <Card className="vertical-collapse">

      <Button className="vertical-collapse-btn" size="sm" onClick={collapse}>
        <FontAwesomeIcon icon="arrow-right" className="menu-collapse-icon"/>
      </Button>

      <CardTitle className="vertical-collapse-title">
        Title
      </CardTitle>
      <CardBody>
        Anim pariatur cliche reprehenderit,
        enim eiusmod high life accusamus terry richardson ad squid. Nihil
        anim keffiyeh helvetica, craft beer labore wes anderson cred
        nesciunt sapiente ea proident.
      </CardBody>
    </Card>
  );
}

export default VerticalCollapse;
