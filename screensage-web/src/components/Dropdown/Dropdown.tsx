/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './Dropdown.module.scss';
import { useEffect, useRef, useState } from 'react';

interface DropdownProps {
  defaultValue: string;
  options: any;
  selectedValue: string;
  onChange: (value: string) => void;
  size?: string;
}

export default function Dropdown({ defaultValue, options, selectedValue, onChange, size }: DropdownProps): React.JSX.Element {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const handleOptionClick = (value: string) => {
    onChange(value);
    setIsOpen(false);
  };

  const handleClickOutside = (event: MouseEvent) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
      setIsOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div 
      className={
        `${s.dropdown} 
        ${size == 'full' ? s.full : size == 'half' ? s.half : size == 'large' ? s.large : size == 'medium' ? s.medium : s.regular}`
      } 
      ref={dropdownRef}
    >
      <div 
        className={s.selectBox} 
        onClick={() => setIsOpen(!isOpen)}
      >
        {/* {selectedValue || `Select ${label}`} */}
        {selectedValue || defaultValue}
        <span className={s.arrow}>{isOpen ? '▲' : '▼'}</span>
      </div>

      {isOpen && options && (
        <ul className={s.optionsList}>
          {options.map((option: any, i: number) => (
            <li
              key={i}
              className={s.optionItem}
              onClick={() => handleOptionClick(option)}
            >
              {option.name || option.title}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};