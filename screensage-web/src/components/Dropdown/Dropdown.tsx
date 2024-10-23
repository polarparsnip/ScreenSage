/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './Dropdown.module.scss';
import { useState } from 'react';

interface DropdownProps {
  defaultValue: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  options: any;
  selectedValue: string;
  onChange: (value: string) => void;
}

export default function Dropdown({ defaultValue, options, selectedValue, onChange }: DropdownProps): React.JSX.Element {
  const [isOpen, setIsOpen] = useState(false);

  const handleOptionClick = (value: string) => {
    onChange(value);
    setIsOpen(false);
  };

  return (
    <div className={s.dropdown}>
      <div 
        className={s.selectBox} 
        onClick={() => setIsOpen(!isOpen)}
      >
        {/* {selectedValue || `Select ${label}`} */}
        {selectedValue || defaultValue}
        <span className={s.arrow}>{isOpen ? "▲" : "▼"}</span>
      </div>

      {isOpen && options && (
        <ul className={s.optionsList}>
          {options.map((option: any) => (
            <li
              key={option.name}
              className={s.optionItem}
              onClick={() => handleOptionClick(option)}
            >
              {option.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};