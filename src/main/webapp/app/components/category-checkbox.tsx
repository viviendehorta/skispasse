import React from 'react'

export const CategoryCheckBox = props => {
  const {id, label, isSelected, value, onCategoryChanged} = props;
  return (
    <div className="form-check-inline">
      <input id={"category" + id}
             className="form-check-input"
             type="checkbox"
             checked={isSelected}
             value={value}
             onChange={onCategoryChanged}/>

      <label className="form-check-label" htmlFor={"category" + id}>
        {label}
      </label>
    </div>
  )
};

export default CategoryCheckBox;
